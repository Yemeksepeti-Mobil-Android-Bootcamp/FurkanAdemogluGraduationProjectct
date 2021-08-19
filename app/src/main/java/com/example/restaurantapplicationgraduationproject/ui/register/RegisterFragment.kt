package com.example.restaurantapplicationgraduationproject.ui.register

import android.animation.Animator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.restaurantapplicationgraduationproject.MainActivity
import com.example.restaurantapplicationgraduationproject.R
import com.example.restaurantapplicationgraduationproject.databinding.FragmentRegisterBinding
import com.example.restaurantapplicationgraduationproject.ui.search.SearchViewModel
import com.example.restaurantapplicationgraduationproject.utils.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var _binding: FragmentRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentRegisterBinding.inflate(inflater, container, false)
            return _binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            _binding.registerButton.setOnClickListener {
                val name = _binding.registerNameTextView.editText?.text.toString()
                val email = _binding.registerEmailTextView.editText?.text.toString()
                val password = _binding.registerPasswordTextView.editText?.text.toString()

                _binding.registerNameTextView.visibility = View.GONE
                _binding.registerEmailTextView.visibility = View.GONE
                _binding.registerPasswordTextView.visibility = View.GONE
                _binding.registerButton.visibility = View.GONE
                _binding.registerAnimation.visibility = View.VISIBLE
                _binding.registerAnimation.setAnimation(R.raw.loading)
                _binding.registerAnimation.playAnimation()


                viewModel.register(name, email, password)
                    .observe(viewLifecycleOwner, Observer {
                        when (it.status) {
                            Resource.Status.LOADING -> {
                                //_binding.progressBar.show()
                            }
                            Resource.Status.SUCCESS -> {
                                //_binding.progressBar.gone()

                                _binding.registerAnimation.setAnimation(R.raw.success)
                                _binding.registerAnimation.playAnimation()
                                _binding.registerAnimation.addAnimatorListener(object :
                                    Animator.AnimatorListener {
                                    override fun onAnimationStart(animation: Animator?) {
                                        Log.v("Animation", "Started")
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {

                                        val action=RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                                        findNavController().navigate(action)
                                    }

                                    override fun onAnimationCancel(animation: Animator?) {
                                        Log.v("Animation", "Canceled")
                                    }

                                    override fun onAnimationRepeat(animation: Animator?) {
                                        Log.v("Animation", "Repeated")
                                    }

                                })

                            }
                            Resource.Status.ERROR -> {
                                //_binding.progressBar.gone()
                                _binding.registerAnimation.setAnimation(R.raw.fail)
                                _binding.registerAnimation.playAnimation()
                                _binding.registerAnimation.addAnimatorListener(object :
                                    Animator.AnimatorListener {
                                    override fun onAnimationStart(animation: Animator?) {
                                        Log.v("Animation", "Started")
                                    }

                                    override fun onAnimationEnd(animation: Animator?) {

                                        _binding.registerAnimation.visibility = View.GONE
                                        _binding.registerNameTextView.visibility = View.VISIBLE
                                        _binding.registerEmailTextView.visibility = View.VISIBLE
                                        _binding.registerPasswordTextView.visibility = View.VISIBLE
                                        _binding.registerButton.visibility = View.VISIBLE

                                        _binding.registerNameTextView.editText?.text?.clear()
                                        _binding.registerEmailTextView.editText?.text?.clear()
                                        _binding.registerPasswordTextView.editText?.text?.clear()
                                    }

                                    override fun onAnimationCancel(animation: Animator?) {
                                        Log.v("Animation", "Canceled")
                                    }

                                    override fun onAnimationRepeat(animation: Animator?) {
                                        Log.v("Animation", "Repeated")
                                    }

                                })
                                val dialog = AlertDialog.Builder(context)
                                    .setTitle("Error")
                                    .setMessage("${it.message}")
                                    .setPositiveButton("Try Again!") { dialog, button ->
                                        dialog.dismiss()
                                    }
                                dialog.show()
                            }
                        }
                    })
            }
        }

    }
